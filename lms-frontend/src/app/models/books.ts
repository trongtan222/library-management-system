export interface Books {
  id: number;
  name: string;
  author: string;
  genre: string;
  publishedYear: number;
  isbn: string;
  // SỬA DÒNG NÀY: đổi noOfCopies thành numberOfCopiesAvailable
  numberOfCopiesAvailable: number; 
  coverUrl?: string;
}