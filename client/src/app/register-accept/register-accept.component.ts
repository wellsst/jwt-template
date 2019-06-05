import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-register-accept',
  templateUrl: './register-accept.component.html',
  styleUrls: ['./register-accept.component.css']
})
export class RegisterAcceptComponent implements OnInit {

  requestId: string;

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

}
