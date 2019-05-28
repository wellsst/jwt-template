import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../auth.service';

@Component({
  selector: 'app-request-jwt',
  templateUrl: './request-jwt.component.html',
  styleUrls: ['./request-jwt.component.css']
})
export class RequestJWTComponent implements OnInit {
  jwt: string;
  loading = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authenticationService: AuthService) {
    this.route.queryParams.subscribe(params => {
      this.jwt = params['jwt'];
      //this.expiresIn = params['expiresIn'];
      console.log('JWT: ' + this.jwt);

    });
  }

  ngOnInit() {

    this.authenticationService.login(this.jwt);
    /*.subscribe(
      () => {*/
    console.log("User is logged in");
    this.router.navigateByUrl('/');
    /* }
   );*/

    /*this.authenticationService.login(this.jwt)
      .subscribe(result => {
        if (result === true) {
          // login successful
          // console.log('requestJWT request ok user is: ' + this.authenticationService.username);
          // this.router.navigate(['/view-history']);
          this.router.navigate(['/login-email-sent']); // todo...change route to
        }
      }, error => {
        this.openSnackBar(error.error + '', '');
      });*/
  }



}
