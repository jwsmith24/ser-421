import {Expression} from "./Module1Activity4A";


export class Calculator {
    result: number;
    history: number[];

    constructor(initialValue: number) {
        this.result = initialValue;
        this.history = [initialValue]; // result stack
    }

    calc(jsonString: string): number {

        // parse the provided json string, handle invalid input
        try {
            const expression: Expression = JSON.parse(jsonString); //throws syntax error if it doesn't match the type

            if (expression.operation === 'add' && typeof (expression.operand) == "number") {
                this.result += expression.operand;
            } else if (expression.operation === 'subtract' && typeof (expression.operand) == "number") {
                this.result -= expression.operand;
            } else {
                throw new Error(`Invalid JSON string provided | operation: ${expression.operation} operand: ${expression.operand}`)
            }
            console.log("got: ", expression, this.result);

        } catch (exception) {
            if (exception instanceof Error) {
                console.error(exception.message);
            }

        }
        this.history = [this.result, ...this.history]; // add latest result first, then spread in the existing results
        return this.result;
    }

   // Leverages the stack
    undo() {
        if (this.history.length  <= 1) {
            console.log("Removing the last result in the stack.. resetting..")
            this.result = 0;
            this.history = [0];
            return this.result;
        } else {
            this.pop();
            this.result = this.history[0]; // update result to previous result
            return this.result;
        }
    }

    peek(n?: number) {

        if (this.history.length === 0 ){
            return null;
        }

        if (n === undefined) {
            // no args provided, return the last result
            return this.history[0];
        }

        // check for valid index
        if (n < 0 || n > this.history.length) {
            return null;
        }

        return this.history[n];
    }

    pop() {
        if (this.history.length === 0) {
            return null;
        }

        return this.history.splice(0, 1)[0]; // splice the first index of the array and then return the number
    }

    printMe() {
        const message = this.history.length > 0 ? "Stack Contents: " + this.history : "The stack is empty";
        console.log(message);
    }

    clear() {
        // reset the stack
        this.history = [0];
        console.log("Results cleared");
    }
}





