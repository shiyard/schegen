# Generate Open Api Schema from a text file

The input file should be located in `/resources` and has the name `entity.txt`.
The file structure should be as following:

`entity.txt`:

Long id;

String firstName;

String lastName;

String email;

LocalDate dateOfBirth;

Double salary;

Boolean isActive;

List<Project> projects;

byte[] picture;

Object object;

---
The output file is located under `/resources/schema.json`.
