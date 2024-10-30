# json-parser
 
A lightweight, custom-built JSON parser in Java designed to parse JSON strings and provide structured outputs. This project includes a tokenizer, parser, and support for JSON data types like objects, arrays, strings, numbers, booleans, and null values. It also validates JSON syntax to ensure compatibility with standard JSON format.

## Features
- Tokenizes JSON strings, supporting braces, brackets, colons, commas, and valid JSON values.
- Parses nested JSON structures including objects and arrays.
- Supports scientific notation for numbers (e.g., 1.23e10) and large numbers.
- Provides validation, ensuring invalid JSON syntax (such as misplaced commas) throws an error.
- Designed to be extensible for future enhancements.

## Getting Started

### Prerequisites

- Java 11 or higher
- Git

1. Clone the repository:
 ```bash
 git clone https://github.com/shivam-hub/json-parser.git
 ```
2. Navigate to the project directory:
```bash
cd json-parser
```

## Compiling the Project
Use the following command to compile all ```.java``` files into an ```out``` directory:
```bash
javac -d out *.java
```

## Running the Parser
Once compiled, you can run the parser by providing a path to a JSON file:
```bash
java -cp out Main <path_to_json_file>
```

## Usage
The ```Main``` class accepts a JSON file path and parses its contents. If a file contains invalid JSON, it throws a detailed error message.
Example:
```bash
java -cp out Main sample.json
```

## Example Inputs
Here are a few examples of JSON structures to test the parser:

```jsonc
// Valid JSON input
{
  "name": "Example",
  "number": 123.456e-789,
  "list": [true, null, 1.2e3]
}

// Invalid JSON input
[123, ,]   // Error due to misplaced comma

```

## Test Suite used:
[JSON Test Suite](https://github.com/nst/JSONTestSuite)
