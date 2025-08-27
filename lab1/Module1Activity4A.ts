export type Expression = {
    operation: "add" | "subtract";
    operand: number;
}

// start with 0
let result = 0;

export function calc(jsonString: string): number {

    // parse the provided json string, handle invalid input
    try {
        const expression: Expression = JSON.parse(jsonString); //throws syntax error if it doesn't match the type

        if (expression.operation === 'add' && typeof (expression.operand) == "number") {
            result += expression.operand;
        } else if (expression.operation === 'subtract' && typeof (expression.operand) == "number") {
            result -= expression.operand;
        } else {
            throw new Error(`Invalid JSON string provided | operation: ${expression.operation} operand: ${expression.operand}`)
        }
        console.log("got: ", expression, result);

    } catch (exception) {
        if (exception instanceof Error) {
            console.error(exception.message);
        }

    }

    return result;
}

/*TEST CASES*/

// test cases from assignment doc:
calc('{ "operation": "add", "operand": 5 }');
calc('{ "operation": "subtract", "operand": 3 }');
calc('{ "operation": "add", "operand": 19 }');

// failing cases
calc('{ "operation": "add", "operand": hi }'); // bad operand
calc('{ "operation": "not real", "operand": 87 }'); // bad operation
calc('{ "something": "add", "anotherThing": 20 }'); // bad props
calc("not a json string"); // invalid json string