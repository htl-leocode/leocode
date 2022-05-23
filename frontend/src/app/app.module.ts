import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {MenuComponent} from './menu/menu.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {PortfolioComponent} from './portfolio/portfolio.component';
import {TestExampleComponent} from './test-example/test-example.component';
import {RouterModule, Routes} from '@angular/router';
import {ListExampleComponent} from './list-example/list-example.component';
import {DetailExampleComponent} from './detail-example/detail-example.component';
import {CreateExampleComponent} from './create-example/create-example.component';
import {FileNotFoundComponent} from './file-not-found/file-not-found.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {LayoutModule} from '@angular/cdk/layout';
import {MatSortModule} from '@angular/material/sort';
import { MatExpansionModule } from '@angular/material/expansion';
import {MatTableModule} from '@angular/material/table';
import {MatIconModule} from '@angular/material/icon';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatListModule} from '@angular/material/list';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {HttpClientModule} from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { SubmissionStatusComponent } from './submission-status/submission-status.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { StudentPageComponent } from './student-page/student-page.component';
import { TeacherPageComponent } from './teacher-page/teacher-page.component';
import { ReportComponent } from './student-page/report/report.component';
import {MatSelectionList} from '@angular/material/list';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import {AuthenticationService} from './authentification/authentication.service';
import {ConfigService} from './services/config.service';
import {OAuthModule} from 'angular-oauth2-oidc';
import {authModuleConfig} from './authentification/oauth-module.config';
import { MonacoEditorModule } from 'ngx-monaco-editor';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { MarkdownModule } from 'ngx-markdown';


const appRoutes: Routes = [
  {path: 'create-example', component: CreateExampleComponent},
  {path: 'test-example', component: TestExampleComponent},
  {path: 'portfolio', component: PortfolioComponent},
  {path: 'list-example/:value', component: ListExampleComponent},
  {path: 'example/:id', component: DetailExampleComponent},
  {path: 'test-example/:id', component: TestExampleComponent},
  {path: 'submission-status/:id', component: SubmissionStatusComponent},
  {path: 'report', component: ReportComponent},
  {path: '',  redirectTo: '', pathMatch: 'full' },
  {path: '**', component: FileNotFoundComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    CreateExampleComponent,
    TestExampleComponent,
    PortfolioComponent,
    ListExampleComponent,
    DetailExampleComponent,
    SubmissionStatusComponent,
    LoginPageComponent,
    StudentPageComponent,
    TeacherPageComponent,
    ReportComponent
  ],
    imports: [
        BrowserModule,
        MatExpansionModule,
        BrowserAnimationsModule,
        LayoutModule,
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatProgressSpinnerModule,
        MatIconModule,
        MatListModule,
        RouterModule.forRoot(appRoutes),
        MarkdownModule.forRoot(),
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        HttpClientModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatRadioModule,
        MatCardModule,
        ReactiveFormsModule,
        FormsModule,
        MonacoEditorModule.forRoot(),
        OAuthModule.forRoot(authModuleConfig),
        ServiceWorkerModule.register('ngsw-worker.js', {
          enabled: environment.production,
          // Register the ServiceWorker as soon as the app is stable
          // or after 30 seconds (whichever comes first).
          registrationStrategy: 'registerWhenStable:30000'
        })
    ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: (auth: AuthenticationService, config: ConfigService) => () =>
        config.init().then(() => auth.initializeLogin()),
      deps: [AuthenticationService, ConfigService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
