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
  emailAddress = '';

  challengeId: string = '';
  cleanupOlderThan: string = '';

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
  }

  signupRequest() {
    this.loading = true;
    this.authenticationService.signupRequest(this.emailAddress)
      .pipe(first())
      .subscribe(result => {
        // login successful
        console.log('Register request ok user is: ' + this.authenticationService.username);
        this.challengeId = result.challengeId;
        this.cleanupOlderThan = result.cleanupOlderThan;
        // this.router.navigate(['/register-email-sent']);
      }, error => {
        this.loading = false;
        console.log(error);
      });
  }
}
