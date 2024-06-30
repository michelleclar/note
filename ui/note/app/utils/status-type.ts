export class BaseType {
    statusCode: number;
    reasonPhrase: string;
    constructor(statusCode: number, reasonPhrase: string) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }
    getStatusCode(): number {
        return this.statusCode;
    }
    getReasonPhrase(): string {
        return this.reasonPhrase;
    }
}