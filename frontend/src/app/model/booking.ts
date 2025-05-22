export interface BookingRequest {
    eventId:number
    userId:number;
    seatsBooked:number;
}

export interface BookingResponse{
    id: number;
    userId: number;
    eventId: number;
    seatsBooked: number;
    bookingDate: string;
    bookingTime: string;
    status: string;
    totalAmount:BigInt;
    fullDateTime?:Date;
}