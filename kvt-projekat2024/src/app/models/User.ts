import { Review } from "./Review";
import { Image } from "./Image";
import { Exercise } from "./Exercise";
import { Manages } from "./Manages";

export class User {
    id: number;
    email: string;
    password: string;
    name: string;
    surname: string;
    createdAt: Date;
    phoneNumber: string;
    birthday: Date;
    address: string;
    city: string;
    zipCode: string;
    reviews: Review[];
    image?: Image; // Make this optional
    exercises: Exercise[];
    manages: Manages[];
    userType: string;

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
        zipCode: string,
        reviews: Review[] = [],
        image?: Image, // Make this optional
        exercises: Exercise[] = [],
        manages: Manages[] = [],
        userType: string = ''
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.createdAt = createdAt;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.reviews = reviews;
        this.image = image; // This can be undefined
        this.exercises = exercises;
        this.manages = manages;
        this.userType = userType;
    }
}
