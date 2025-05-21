export interface Events {
    id: number;
    eventName: string;
    description: string;
    location: string;
    date: string;         
    time: string;          
    price: number;
    availableSeats: number;
    organizerName: string;
    imageUrl: string;
    status: 'ACTIVE' | 'INACTIVE' | 'CANCELLED' | string;  
    createdAt: string;     
    updatedAt: string; 
    fullDateTime?: Date;
}
