package getSourceCode21052018;

import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class Trip extends ArrayList<ScheduleEntry> implements Serializable
/*    */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*    */   int veh;
/*    */   int day;
/*    */   int volume;
/*    */   double volScore;
/*    */   boolean full;
/*    */   ScheduleEntry first;
/*    */   ScheduleEntry last;
/*    */ 
/*    */   public Trip(int d, int v)
/*    */   {
/* 18 */     this.day = d;
/* 19 */     this.veh = v;
/* 20 */     this.volume = 0;
/* 21 */     this.full = false;
/*    */ 
/* 23 */     this.first = new ScheduleEntry(this.day, this.veh, new Order(0, 0, 0, 0.0D, 287), new ArrayList());
/* 24 */     this.first.trip = this;
/* 25 */     this.first.previous = new ScheduleEntry(0);
/*    */ 
/* 27 */     this.last = new ScheduleEntry(this.day, this.veh, new Order(0, 0, 0, 1800.0D, 287), new ArrayList());
/* 28 */     this.last.trip = this;
/* 29 */     this.last.next = new ScheduleEntry(0);
/* 30 */     add(this.last);
/*    */ 
/* 32 */     this.last.previous = this.first;
/* 33 */     this.first.next = this.last;
/*    */   }
/*    */ 
/*    */   public boolean hasEnoughSpaceFor(int vol) {
/* 37 */     if (this.volume + vol > 100000) {
/* 38 */       return false;
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   public double volScoreDiffWouldBe(int vol)
/*    */   {
/* 45 */     if (vol == -1) {
/* 46 */       return -Math.max(0, (this.volume - 100000) * 5);
/*    */     }
/* 48 */     return Math.max(0, this.volume + vol - 100000) * 5 - this.volScore;
/*    */   }
/*    */ 
/*    */   public void updateVolume(int vol)
/*    */   {
/* 53 */     this.volume += vol;
/* 54 */     this.volScore = (Math.max(0, this.volume - 100000) * 5);
/* 55 */     if (this.volume >= 100000)
/* 56 */       this.full = true;
/*    */     else
/* 58 */       this.full = false;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 64 */     String s = "";
/* 65 */     ScheduleEntry se = this.first;
/*    */ 
/* 67 */     while (se.hasNext()) {
/* 68 */       se = se.next;
/* 69 */       s = s + se.order.toString();
/*    */     }
/* 71 */     return s;
/*    */   }
/*    */ }