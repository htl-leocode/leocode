import {Teacher} from "./teacher.model";

export interface Repository {

  id: number;
  name: string;
  teacher: Teacher;

}
