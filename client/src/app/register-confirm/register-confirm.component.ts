import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../auth.service";
import {first} from "rxjs/operators";

@Component({
  selector: 'app-register-confirm',
  templateUrl: './register-confirm.component.html',
  styleUrls: ['./register-confirm.component.css']
})
export class RegisterConfirmComponent implements OnInit {

  requestId: string;
  challengeId: string;
  message: string;

  jwt:string;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthService) {
    this.route.queryParams.subscribe(params => {
      this.requestId = params['requestId'];
      console.log('requestId: ' + this.requestId);
    });
  }

  ngOnInit() {
  }

  registerConfirm() {
    this.authenticationService.registerAccept(this.requestId, this.challengeId)
      .pipe(first())
      .subscribe(result => {
        // login successful
        console.log('Register accepted ok user is: ' + this.authenticationService.username);

        if (result.jwt) {
          this.jwt = result.jwt;
          this.authenticationService.login(this.jwt);
        } else {
          this.message = "Error in receiving the JWT, please try again"
        }
      }, error => {
        console.log(error );
        this.message = error;
      });
  }
}


