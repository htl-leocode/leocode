import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpService} from '../services/http.service';
import {AuthenticationService} from '../authentification/authentication.service';


class Language{
  viewValue: string;
  language: string;
  constructor( viewValue: string, language: string) {
    this.viewValue = viewValue;
    this.language = language;
  }
}

@Component({
  selector: 'app-test-example',
  templateUrl: './test-example.component.html',
  styleUrls: ['./test-example.component.css']
})
export class TestExampleComponent implements OnInit {
  editorOptions = {
    language: 'java',
    colors: {
      'editor.foreground': '#000000',
      'editor.background': '#FFFFFF',
      'editorCursor.foreground': 'red',
      'editor.lineHighlightBackground': '#1600FF21',
      'editorLineNumber.foreground': '#008800',
      'editor.selectionBackground': '#88000030',
      'editor.inactiveSelectionBackground': '#88000015'
    },
    automaticLayout: true
  };

  codeJava = 'package at.htl.examples;\npublic class HelloWorld {\n' +
    '\n' +
    '    public static void main(String[] args) {\n' +
    '        System.out.println("Hello, World");\n' +
    '    }\n' +
    '\n' +
    '}';

  codeCsharp = 'using System;\n' +
    '\t\t\t\t\t\n' +
    'public class Program\n' +
    '{\n' +
    '\tpublic static void Main()\n' +
    '\t{\n' +
    '\t\tConsole.WriteLine("Hello World");\n' +
    '\t}\n' +
    '}';

  codeKotlin = 'fun main() {\n' +
    '    println("Hello, World!")\n' +
    '}';


  code = '';

  fileIsUploaded = false;

  uploadedFile: File | undefined;

  markDownFileContent = '';

  exampleDescription = '';

  exampleId: number = 0;
  username = '';
  form: FormData | undefined;

  languages: Language[] = [
    new Language(this.codeCsharp, 'csharp'),
    new Language(this.codeJava, 'java'),
    new Language(this.codeKotlin, 'kotlin')

  ];

  defaultLang: Language;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: HttpService,
              private authService: AuthenticationService) {
    if(this.languages.find(l => l.language === 'java') == undefined) {
      console.log("java not found in languages list")
      this.defaultLang = this.languages[0];
      return;
    }
    this.defaultLang = this.languages.find(l => l.language === 'java')!;
    this.code = this.defaultLang.viewValue;
  }

  ngOnInit(): void {
    this.username = this.authService.username.getValue();
    this.checkPathParam();
    this.http.getExampleById(this.exampleId).subscribe(value => {
        if(value.files.find(f => f.fileType === 'INSTRUCTION') == undefined) {
          throw new Error("instruction file not found!")
        }
        this.markDownFileContent = value.files.find(f => f.fileType === 'INSTRUCTION')!.content;
        this.exampleDescription = value.description;
    }, error => {
      console.log(error);
    });
  }

  checkPathParam(): boolean {
    const res = this.route.snapshot.paramMap.get('id');
    if(res == null) {
      return false;
    }
    this.exampleId = +res;
    if (0 !== this.exampleId) {
      return true;
    } else {
      return false;
    }
  }

  sendSubmission(): void {
    if (document.forms.namedItem('testExampleForm') == null) {
      console.log("testExampleForm form element not found");
    }
    this.form = new FormData(document.forms.namedItem('testExampleForm')!);
    if (this.checkPathParam()) {
      this.form.set('example', String(this.exampleId));
    }
    const file = new File([this.code], 'HelloWorld.java', {type: 'text/plain', });
    this.form.set('code', file);
    this.form.set('username', this.username);
    console.log(this.form);
    console.log(this.form.get('code'));
    this.http.testExample(this.form).subscribe(value => {
      this.router.navigate(['submission-status', value]);
    });
  }
  changeCode(value: any): void {
    if (this.code === '' || this.code === this.codeCsharp || this.code === this.codeJava || this.code === this.codeKotlin){
      this.code = value;
    }

    if(this.languages.find(l => l.viewValue === value) == undefined || this.languages.find(l => l.viewValue === value) == undefined) {
      console.log("Language not found");
      return;
    }

    // Muss gemacht werden, dass Editor die Änderung der Sprache übernimmt
    this.editorOptions = {...this.editorOptions, language: this.languages.find(l => l.viewValue === value)!.language};
    Object.assign({}, this.editorOptions, {language: this.languages.find(l => l.viewValue === value)!.language});
  }

  async uploadFile(event: Event): Promise<void> {
    const target = event.target as HTMLInputElement;
    const files = target.files as FileList;
    if(files.item(0) == null){
      return;
    }
    this.code = await files.item(0)!.text();
    this.uploadedFile = files.item(0)!;
    this.fileIsUploaded = true;
    console.log(this.fileIsUploaded);
  }

  downloadFile(): void {
    if(this.uploadedFile == undefined){
      console.log("Uploaded-file is undefined!");
      return;
    }
    const file = new File([this.code], this.uploadedFile.name, {type: 'text/plain', });
    const link = document.createElement('a');
    link.download = file.name;
    link.href = URL.createObjectURL(file);
    link.target = '_blank';
    link.click();
  }

}
