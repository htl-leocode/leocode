import { FailureDetails } from "./failureDetail.model"

export interface TestCase{
    name: string,
    classname:string,
    time:string,
    failure: FailureDetails
}

