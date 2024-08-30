import { User } from "./User";

export class Administrator extends User {
  constructor(
    id: number,
    email: string,
    password: string,
    name: string,
    surname: string,
    createdAt: Date,
    phoneNumber: string,
    birthday: Date,
    address: string,
    city: string,
    zipCode: string
  ) {
    super(
      id,
      email,
      password,
      name,
      surname,
      createdAt,
      phoneNumber,
      birthday,
      address,
      city,
      zipCode
    );
  }
}
