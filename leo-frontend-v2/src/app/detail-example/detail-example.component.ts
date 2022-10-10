import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatTable, MatTableDataSource} from '@angular/material/table';
import { DetailExampleDataSource, DetailExampleItem } from './detail-example-datasource';
import {Example} from '../model/example.model';
import {HttpService} from '../services/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {LeoCodeFile} from "../model/leocodefile.model";

@Component({
  selector: 'app-detail-example',
  templateUrl: './detail-example.component.html',
  styleUrls: ['./detail-example.component.css']
})
export class DetailExampleComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<Example>;
  dataSource: MatTableDataSource<Example>;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name', 'description', 'type', 'files', 'test'];

  markDownFileContent = '';

  constructor(private route: ActivatedRoute,
              public router: Router,
              private http: HttpService) {
  }

  async ngOnInit(): Promise<void> {
    this.dataSource = new MatTableDataSource<Example>();
    await this.refreshData(+this.route.snapshot.paramMap.get('id'));
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }


  refreshData(id: number): void {
    // because just one value is returned but material requires array
    this.http.getExampleById(id).subscribe(value => {
      if (value === null) {
        this.router.navigate(['NotFound']);
      } else {
        this.dataSource.data = [value];
        this.markDownFileContent = this.dataSource.data[0].files.find(f => f.fileType === 'INSTRUCTION').content;
      }
    }, error => {
      console.log(error);
    });
  }

  createURLForFile(file: File): string {
    return URL.createObjectURL(file);
  }
}
