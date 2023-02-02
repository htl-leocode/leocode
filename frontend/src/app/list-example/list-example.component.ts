import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import {ListExampleDataSource, ListExampleItem} from './list-example-datasource';
import {Example} from '../model/example.model';
import {HttpService} from '../services/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatGridListModule} from '@angular/material/grid-list';

@Component({
  selector: 'app-list-example',
  templateUrl: './list-example.component.html',
  styleUrls: ['./list-example.component.css']
})
export class ListExampleComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<Example>;
  examples: Example[];
  value: number;

  selectedType: string = "";
  selectedTypebool: boolean = true;

  public types: string[] = []
  public selectedTypes: string[] = []

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['name', 'description', 'type'];

  constructor(private http: HttpService,
              public router: Router,
              private route: ActivatedRoute) {
    this.value = Number(this.route.snapshot.paramMap.get('value'));
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource<Example>();
    this.refreshData();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  refreshData(): void {
    this.http.getExampleList().subscribe(exampleList => this.dataSource.data = exampleList, error => console.log(error));
    this.http.getExampleList().subscribe(exampleList => {
      this.examples = exampleList;
      this.getAllTypes();
    } ,error => console.log(error));
  }

  getAllTypes() {
    this.examples.forEach((i) => {
      if (!this.types.includes(i.type)) {  // if type is not in the list
        this.types.push(i.type);
      }
    });
  }

  setType(type: string) {
    if(!this.selectedTypes.includes(type) ){
        this.selectedTypes.push(type);
    }
    else {
      this.selectedTypes = this.selectedTypes.filter(currentType => currentType != type)
    }


  }
}
