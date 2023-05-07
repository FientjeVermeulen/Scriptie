package getSourceCode21052018;

import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ScheduleEntry implements Serializable
/*    */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*    */   int day;
/*    */   int vehicle;
/*    */   int key;
/*    */   Order order;
/*    */   Trip trip;
/*    */   ScheduleEntry previous;
/*    */   ScheduleEntry next;
/*    */   List<ScheduleEntry> copies;
/*    */ 
/*    */   public ScheduleEntry(int dag2, int vehicle2, Order ordr, ArrayList<ScheduleEntry> cops)
/*    */   {
/* 18 */     this.day = dag2;
/* 19 */     this.vehicle = vehicle2;
/* 20 */     this.order = ordr;
/* 21 */     this.copies = cops;
/*    */   }
/*    */ 
/*    */   public ScheduleEntry(int empty) {
/* 25 */     this.order = new Order(-1, -1, -1, -1.0D, -1);
/* 26 */     this.day = -1;
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 30 */     if (this.next.day == -1) {
/* 31 */       return false;
/*    */     }
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 39 */     String s = "ScheduleEntry: day: " + this.day + " vehicle: " + this.vehicle + " key: " + this.key + "\n";
/* 40 */     s = s + "Order: " + this.order.toString();
/* 41 */     s = s + "\t previous: " + this.previous.order.toString();
/* 42 */     s = s + "\t next: " + this.next.order.toString();
/* 43 */     return s;
/*    */   }
/*    */ }