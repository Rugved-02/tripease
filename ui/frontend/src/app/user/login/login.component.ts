import { Component, inject, NgModule } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { Router } from '@angular/router';
import { MenubarComponent } from '../../shared/components/menubar/menubar.component';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageModule } from 'primeng/message';
import { MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [InputTextModule, ButtonModule, FloatLabelModule, PasswordModule, MenubarComponent, CardModule,
    ToastModule, MessageModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  providers: [MessageService]
})
export class Login {
  // constructor(private router:Router){}

  messageService = inject(MessageService);

  exampleForm: FormGroup;

  formSubmitted = false;

  constructor(private fb: FormBuilder, private router: Router) {
    this.exampleForm = this.fb.group({
      username: ['', Validators.required],
      value: ['', Validators.required]
    });
  }

  onSubmit() {
    this.formSubmitted = true;
    if (this.exampleForm.valid) {
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Form Submitted', life: 3000 });
      this.exampleForm.reset();
      this.formSubmitted = false;
    }
  }

  isInvalid(controlName: string) {
    const control = this.exampleForm.get(controlName);
    return control?.invalid && (control.touched || this.formSubmitted);
  }

  register() {
    this.router.navigate(['/register'])
  }
}
