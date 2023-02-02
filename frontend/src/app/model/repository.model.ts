import {Teacher} from "./teacher.model";

export interface Repository {

  id: number;
  repoUrl: string;
  teacher: Teacher;

}
