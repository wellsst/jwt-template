import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';

import {AuthService} from '../auth.service';
import {MatSnackBar} from '@angular/material';

/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  // moduleId: module.id,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  /* emailFormControl = new FormControl('', [
     Validators.required,
     Validators.email,
   ]);
   passwordFormControl = new FormControl('', [
     Validators.required
   ]);
 */
  matcher = new MyErrorStateMatcher();

  /*formGroup: FormGroup;
*/
  loading = false;
  error = '';
  model = {email: '', password: ''};

  constructor(
    public snackBar: MatSnackBar,
    // private fb: FormBuilder,
    private router: Router,
    private authenticationService: AuthService) {
    /*this.formGroup = this.fb.group({
      email:  new FormControl(),
      password:  new FormControl()
    })*/
  }

  ngOnInit() {
    // reset login status
    this.authenticationService.logout();
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 10000,
    });
  }

  login() {
    this.loading = true;
    /*this.authenticationService.login(this.model.email, this.model.password)
      .subscribe(result => {
        if (result === true) {
          // login successful
          console.log('Login request ok user is: ' + this.authenticationService.username);
         // this.router.navigate(['/view-history']);
          this.router.navigate(['/login-email-sent']);
        }
      }, error => {
        this.loading = false;
        this.openSnackBar(error.error + '', '');
        console.log(error.error );
      });*/
  }

  signupRequest() {
    this.loading = true;
    this.authenticationService.signupRequest(this.model.email)
      .subscribe(result => {
        if (result.status === "OK") {
          // login successful
          console.log('Login request ok user is: ' + this.authenticationService.username);
          // this.router.navigate(['/view-history']);
          this.router.navigate(['/login-email-sent']);
        }
      }, error => {
        this.loading = false;
        this.openSnackBar(error.error + '', '');
        console.log(error.error );
      });
  }


}
