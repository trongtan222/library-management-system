package com.ibizabroker.lms.dto;
public class ReservationRequest {
  private Integer bookId; private Integer memberId;
  public Integer getBookId(){ return bookId; }  public void setBookId(Integer v){ this.bookId=v; }
  public Integer getMemberId(){ return memberId; } public void setMemberId(Integer v){ this.memberId=v; }
  private Integer quantity;      // Số lượng đặt chỗ
  private String studentName;    // Tên học sinh (dùng để đối chiếu/ghi chú)
  private String studentClass;   // Lớp học sinh

  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }

  public String getStudentName() { return studentName; }
  public void setStudentName(String studentName) { this.studentName = studentName; }

  public String getStudentClass() { return studentClass; }
  public void setStudentClass(String studentClass) { this.studentClass = studentClass; }
}
