import { Component, OnInit } from '@angular/core';
import gql from 'graphql-tag';
import {Apollo} from 'apollo-angular';
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

const GET_USERS = gql`{
userList(max: 10) {
    id
    username
    registrationRequest {
      requestId
      dateCreated
      challengeId
    }
  }
}
`;

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: Observable<any>;
  constructor(private apollo: Apollo) { }

  ngOnInit() {
    this.users = this.apollo.watchQuery({
        query: GET_USERS,
      })
      .valueChanges.pipe(map(result => result.data && result.data.userList));
  }

}
