import {Component, Inject, OnInit} from '@angular/core';
import {DOCUMENT} from "@angular/common";

@Component({
  selector: 'app-secure-check',
  templateUrl: './secure-check.component.html',
  styleUrls: ['./secure-check.component.css']
})
export class SecureCheckComponent implements OnInit {

  public warningMsg: string = "";

  constructor(@Inject(DOCUMENT) private document) {}

  ngOnInit() {
    /*
    The object contains info -
    {
      "href": "http://localhost:4200/",
      "ancestorOrigins": {},
      "origin": "http://localhost:4200",
      "protocol": "http:",
      "host": "localhost:4200",
      "hostname": "localhost",
      "port": "4200",
      "pathname": "/",
      "search": "",
      "hash": ""
    }
     */
    if (!document.location.protocol.startsWith("https")) {
      this.warningMsg = "This connection is not running securely - for JWT to work securely there must be a secure connection between client and server"
    }
  }

}
