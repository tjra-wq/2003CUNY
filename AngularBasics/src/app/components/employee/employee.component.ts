import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {

  constructor() { }

  @Input() name:string= "McDefault"
  @Input() title:string = "Job stuff";
  @Input() contactInfo:number =5555555555

  ngOnInit(): void {

  }

}
