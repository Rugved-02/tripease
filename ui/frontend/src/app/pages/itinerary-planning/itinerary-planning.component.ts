import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { TimelineModule } from 'primeng/timeline';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { DatePickerModule } from 'primeng/datepicker';
import { TextareaModule } from 'primeng/textarea';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { ItineraryService } from '../../core/services/itinerary/itinerary.service';

// Updated to match your Backend DTOs
interface Itinerary {
  id?: number;
  tripName: string;
  startDate: string;
  endDate: string;
  activities: any[];
}

@Component({
  selector: 'app-itinerary-planning',
  standalone: true,
  imports: [
    CommonModule, 
    TimelineModule, 
    ButtonModule, 
    ReactiveFormsModule, 
    DialogModule, 
    SelectModule, 
    InputTextModule,
    DatePickerModule,
    TextareaModule
  ],
  templateUrl: './itinerary-planning.component.html',
  styleUrl: './itinerary-planning.component.css'
})
export class ItineraryPlanningComponent implements OnInit {

  private itineraryService = inject(ItineraryService);
  private fb = inject(FormBuilder);

  itineraries: Itinerary[] = [];
  displayDialog: boolean = false;
  tripForm: FormGroup;
  isEditing: boolean = false;
  editIndex: number | null = null;

  // Matching your Java ActivityType Enum
  iconOptions = [
    { label: 'Flight', value: 'FLIGHT' },
    { label: 'Hotel', value: 'HOTEL' },
    { label: 'Location', value: 'LOCATION' },
    { label: 'Sightseeing', value: 'SIGHTSEEING' },
    { label: 'Restaurant', value: 'RESTAURANT' },
    { label: 'Transport', value: 'TRANSPORT' },
    { label: 'Work', value: 'WORK' },
    { label: 'Other', value: 'OTHER' }
  ];

  constructor() {
    this.tripForm = this.fb.group({
      tripName: ['', Validators.required],
      startDate: [null, Validators.required],
      endDate: [null, Validators.required],
      activities: this.fb.array([])
    });
  }

  ngOnInit() {
    this.loadTrips();
  }


    loadTrips() {
      this.itineraryService.getMyTrips().subscribe({
        next: (trips: any[]) => {
          this.itineraries = trips.map(trip => ({
            // Map any variation of ID coming from Java Jackson serialization
            id: trip.id || trip.tripId || trip.trip_id, 
            tripName: trip.tripName,
            startDate: trip.startDate,
            endDate: trip.endDate,
            activities: trip.activities || []
          }));
          console.log('Successfully loaded and mapped itineraries:', this.itineraries);
        },
        error: (err) => console.error('Error fetching trips:', err)
      });
    }


  get activities() {
    return this.tripForm.get('activities') as FormArray;
  }


  createActivityGroup(data?: any): FormGroup {
    return this.fb.group({
      // Store the ID so the backend knows which existing activity to update/keep
      activityId: [data?.activityId || null],
      title: [data?.title || '', Validators.required],
      type: [data?.type || 'SIGHTSEEING', Validators.required],
      location: [data?.location || '', Validators.required],
      startTime: [data?.startTime ? new Date(data.startTime) : null, Validators.required],
      endTime: [data?.endTime ? new Date(data.endTime) : null, Validators.required],
      notes: [data?.notes || '']
    });
  }

      saveFullItinerary() {
        if (this.tripForm.invalid) {
          this.tripForm.markAllAsTouched();
          return;
        }

        const formValue = this.tripForm.value;

        // 1. Prepare the full payload (Metadata + Activities)
        const tripRequest = {
          tripName: formValue.tripName,
          startDate: this.formatDate(formValue.startDate),
          endDate: this.formatDate(formValue.endDate),
          // Ensure activities are included in the update request
          activities: formValue.activities.map((act: any) => ({
            activityId: act.activityId, // Keep ID if it exists for updates
            title: act.title,
            type: act.type,
            location: act.location,
            startTime: act.startTime instanceof Date ? act.startTime.toISOString() : new Date(act.startTime).toISOString(),
            endTime: act.endTime instanceof Date ? act.endTime.toISOString() : new Date(act.endTime).toISOString(),
            notes: act.notes
          }))
        };

        if (this.isEditing && this.editIndex !== null) {
          const tripId = this.itineraries[this.editIndex].id;

          if (!tripId) {
            console.error('Update aborted: Trip ID is missing.');
            alert('Error: Missing Trip ID. Please refresh the page.');
            return;
          }

          // Pass the combined request (metadata + activities)
          this.itineraryService.updateTrip(tripId, tripRequest).subscribe({
            next: (updatedTripFromServer) => {
              console.log('Update successful');

              // Refresh the list from server to ensure data integrity
              this.loadTrips(); 

              // Fix NG0100 error by wrapping UI state changes in setTimeout
              setTimeout(() => {
                this.displayDialog = false;
                this.isEditing = false;
                this.editIndex = null;
                this.tripForm.reset();
              });
            },
            error: (err) => {
              console.error('Update failed:', err);
              alert('Could not update trip. Please check your connection.');
            }
          });

        } else {
          // CREATE NEW TRIP logic
          // Using tripRequest.activities here since it's already processed
          this.itineraryService.saveFullTrip(tripRequest, tripRequest.activities).subscribe({
            next: () => {
              this.loadTrips();
              setTimeout(() => {
                this.displayDialog = false;
                this.tripForm.reset();
              });
            },
            error: (err) => console.error('Save failed:', err)
          });
        }
      }



  // Helper to format Date to YYYY-MM-DD for LocalDate backend
  private formatDate(date: Date): string {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }

    deleteItinerary(index: number) {
        console.log('Index received:', index);
        const trip = this.itineraries[index];

        if (!trip) {
            console.error('No trip found at index', index);
            return;
        }

        const tripId = trip.id;
        console.log('Attempting to delete Trip ID:', tripId);

        if (tripId && confirm('Are you sure?')) {
            this.itineraryService.deleteTrip(tripId).subscribe({
                next: () => {
                    console.log('Delete successful on server');
                    this.itineraries = this.itineraries.filter((_, i) => i !== index);
                },
                error: (err) => {
                    console.error('Delete failed on server:', err);
                }
            });
        }
    }

  // --- Dialog & Helper Controls ---

  showDialog() {
    this.isEditing = false;
    this.editIndex = null;
    this.tripForm.reset();
    this.activities.clear();
    this.addActivityRow(); 
    this.displayDialog = true;
  }

  editItinerary(index: number) {
        this.isEditing = true;
        this.editIndex = index;
        const selectedTrip = this.itineraries[index];

        // DEBUG: Check if ID exists right now
        console.log('Editing Trip:', selectedTrip);

        if (!selectedTrip.id) {
            console.warn('Warning: Selected trip has no ID. Update will fail!');
        }

        this.activities.clear();
        this.tripForm.patchValue({
          tripName: selectedTrip.tripName,
          startDate: selectedTrip.startDate ? new Date(selectedTrip.startDate) : null,
          endDate: selectedTrip.endDate ? new Date(selectedTrip.endDate) : null
        });

        if (selectedTrip.activities) {
          selectedTrip.activities.forEach(act => {
            this.activities.push(this.createActivityGroup(act));
          });
        }

        this.displayDialog = true;
    }

  addActivityRow() {
    this.activities.push(this.createActivityGroup());
  }

  removeActivityRow(index: number) {
    this.activities.removeAt(index);
  }

  downloadPDF(index: number) {
    const trip = this.itineraries[index];
    const doc = new jsPDF();

    doc.setFontSize(20);
    doc.text(trip.tripName, 14, 22);

    doc.setFontSize(12);
    doc.text(`Duration: ${trip.startDate} to ${trip.endDate}`, 14, 30);

    const tableColumn = ["Time (Start)", "Activity", "Location", "Type"];

    const tableRows = trip.activities.map(act => [
      new Date(act.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      act.title,
      act.location,
      act.type
    ]);

    autoTable(doc, {
      head: [tableColumn],
      body: tableRows,
      startY: 40,
      theme: 'grid',
      headStyles: { fillColor: [15, 23, 42] }
    });

    doc.save(`${trip.tripName.replace(/\s+/g, '_')}_Itinerary.pdf`);
  }

  shareItinerary(index: number) {
    const trip = this.itineraries[index];
    alert(`Sharing ${trip.tripName} itinerary!`);
  }
}
 