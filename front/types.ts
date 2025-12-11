export interface Item {
  id: string;
  name: string;
  description: string;
  price: number;
  image: string;
  rating?: number; // legacy
  ratingAvg?: number;
  ratingCount?: number;
  type: string;
  ownerId?: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  address?: string;
  city?: string;
  avatar?: string;
}

export type ScreenName = 
  | 'login' 
  | 'register' 
  | 'address' 
  | 'home' 
  | 'add' 
  | 'edit-item'
  | 'profile' 
  | 'detail'
  | 'rental-list'
  | 'rental-status';

export interface Rental {
  id: string;
  itemId: string;
  itemName: string;
  itemImage: string;
  status: 'pending' | 'sent' | 'arrived' | 'returned';
  startDate: string;
  endDate: string;
  days?: number;
  totalPrice?: number;
}