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
    if (!document.location.protocol.startsWith("https")) {
      this.warningMsg = "This connection is not running securely - for JWT to work securely there must be a secure connection between client and server"
    }
  }

}
