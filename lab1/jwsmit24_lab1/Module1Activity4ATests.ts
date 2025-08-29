import {calc} from "./Module1Activity4A";


// test cases from assignment doc:
calc('{ "operation": "add", "operand": 5 }');
calc('{ "operation": "subtract", "operand": 3 }');
calc('{ "operation": "add", "operand": 19 }');

// failing cases
calc('{ "operation": "add", "operand": hi }'); // bad operand
calc('{ "operation": "not real", "operand": 87 }'); // bad operation
calc('{ "something": "add", "anotherThing": 20 }'); // bad props
calc("not a json string"); // invalid json string