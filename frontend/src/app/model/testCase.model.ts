import { FailureDetails } from "./failureDetail.model"

export interface TestCase{
    name: string,
    className:string,
    time:string,
    failure: FailureDetails
}

