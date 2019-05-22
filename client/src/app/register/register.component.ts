import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../auth.service";
import {first} from "rxjs/operators";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  loading = false;
  emailAddress = ''

  constructor(
    private router: Router,
    private authenticationService: AuthService) {
  }

  ngOnInit() {
    // reset login status
    this.authenticationService.logout();
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
    this.authenticationService.signupRequest(this.emailAddress)
      .pipe(first())
      .subscribe(result => {
        // login successful
        console.log('Register request ok user is: ' + this.authenticationService.username);
        // this.router.navigate(['/view-history']);
        this.router.navigate(['/register-email-sent']);
      }, error => {
        this.loading = false;
        console.log(error.error );
      });
  }
}
