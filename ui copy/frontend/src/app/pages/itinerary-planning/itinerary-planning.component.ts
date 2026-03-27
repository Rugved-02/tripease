import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { TimelineModule } from 'primeng/timeline';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select'; // Ensure you are on PrimeNG v18+ for this
import { InputTextModule } from 'primeng/inputtext';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

interface Itinerary {
  tripName: string;
  dateRange: string;
  events: any[];
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
    InputTextModule
  ],
  templateUrl: './itinerary-planning.component.html',
  styleUrl: './itinerary-planning.component.css'
})
export class ItineraryPlanningComponent implements OnInit {
  // Standard properties (No Signals)
  itineraries: Itinerary[] = [];
  displayDialog: boolean = false;
  tripForm: FormGroup;
  isEditing: boolean = false;
  editIndex: number | null = null;

  iconOptions = [
    { label: 'Flight', value: 'flight' },
    { label: 'Hotel', value: 'hotel' },
    { label: 'Meeting', value: 'location' }
  ];

  constructor(private fb: FormBuilder) {
    this.tripForm = this.fb.group({
      tripName: ['', Validators.required],
      dateRange: ['', Validators.required],
      activities: this.fb.array([])
    });
  }

  ngOnInit() {
    this.itineraries = [{
      tripName: 'Los Angeles Business Trip',
      dateRange: 'Feb 15, 2026 → Feb 20, 2026',
      events: [
        { title: 'Flight Departure - DL 1234', subtitle: 'Departure from JFK to LAX', time: '08:00 AM', iconType: 'flight', color: '#3B82F6' },
        { title: 'Hotel Check-in', subtitle: 'Grand Plaza Hotel, Los Angeles', time: '11:30 AM', iconType: 'hotel', color: '#6366F1' },
        { title: 'Business Meeting', subtitle: 'Downtown Convention Center', time: '03:30 PM', iconType: 'location', color: '#A855F7' }
      ]
    }];
  }

  get activities() {
    return this.tripForm.get('activities') as FormArray;
  }

  createActivityGroup(data?: any): FormGroup {
    return this.fb.group({
      title: [data?.title || '', Validators.required],
      subtitle: [data?.subtitle || '', Validators.required],
      time: [data?.time || '', Validators.required],
      iconType: [data?.iconType || 'location', Validators.required]
    });
  }

  // --- Actions ---

  deleteItinerary(index: number) {
    // this.itineraries.splice(index, 1);
    this.itineraries = this.itineraries.filter((_, i) => i !== index);
  }

  shareItinerary(index: number) {
    const trip = this.itineraries[index];
    alert(`Sharing ${trip.tripName} itinerary!`);
  }

  downloadPDF(index: number) {
    const trip = this.itineraries[index];
    const doc = new jsPDF();

    doc.setFontSize(20);
    doc.setTextColor(40);
    doc.text(trip.tripName, 14, 22);
    
    doc.setFontSize(12);
    doc.setTextColor(100);
    doc.text(`Dates: ${trip.dateRange}`, 14, 30);

    const tableColumn = ["Time", "Activity", "Description", "Type"];
    
    const tableRows = trip.events.map(event => [
      event.time,
      event.title,
      event.subtitle,
      event.iconType.toUpperCase()
    ]);

    autoTable(doc, {
      head: [tableColumn],
      body: tableRows,
      startY: 40,
      theme: 'grid',
      headStyles: { fillColor: [15, 23, 42] },
      styles: { fontSize: 10, cellPadding: 5 },
      columnStyles: {
        0: { cellWidth: 30 },
        1: { fontStyle: 'bold' }
      }
    });

    const fileName = `${trip.tripName.replace(/\s+/g, '_')}_Itinerary.pdf`;
    doc.save(fileName);
  }

  // --- Form Controls ---

  addActivityRow() {
    this.activities.push(this.createActivityGroup());
  }

  removeActivityRow(index: number) {
    this.activities.removeAt(index);
  }

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

    this.activities.clear();
    this.tripForm.patchValue({
      tripName: selectedTrip.tripName,
      dateRange: selectedTrip.dateRange
    });

    selectedTrip.events.forEach(event => {
      this.activities.push(this.createActivityGroup(event));
    });

    this.displayDialog = true;
  }

  saveFullItinerary() {
  if (this.tripForm.valid) {
    const rawValues = this.tripForm.value;
    
    // Process colors based on selection
    const processedEvents = rawValues.activities.map((act: any) => ({
      ...act,
      color: act.iconType === 'flight' ? '#3B82F6' : act.iconType === 'hotel' ? '#6366F1' : '#A855F7'
    }));

    const newItinerary: Itinerary = {
      tripName: rawValues.tripName,
      dateRange: rawValues.dateRange,
      events: processedEvents
    };

    if (this.isEditing && this.editIndex !== null) {
      // EDIT: Create a copy of the array, update the item, then reassign
      const updatedItineraries = [...this.itineraries];
      updatedItineraries[this.editIndex] = newItinerary;
      this.itineraries = updatedItineraries; // New Reference triggers view update
    } else {
      // ADD: Create a new array containing old items + new item
      this.itineraries = [...this.itineraries, newItinerary];
    }

    this.displayDialog = false;
  }
}
}