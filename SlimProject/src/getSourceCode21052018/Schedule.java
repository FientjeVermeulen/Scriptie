package getSourceCode21052018;

		 import java.io.Serializable;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ public class Schedule implements Serializable
/*    */ {
/*    */   double endTime;
/*    */   double timeScore;
/*    */   int vehicle;
/*    */   int day;
/*    */   LinkedList<Trip> trips;
/*    */   boolean noTripAdded;
/*    */   private static final long serialVersionUID = 752647229562276147L;
/*    */ 
/*    */   public Schedule(int d, int veh)
/*    */   {
/* 19 */     this.vehicle = veh;
/* 20 */     this.day = d;
/* 21 */     this.endTime = 0.0D;
/* 22 */     this.trips = new LinkedList();
/* 23 */     addNewTrip();
/* 24 */     this.noTripAdded = true;
/*    */   }
/*    */ 
/*    */   public void addNewTrip() {
/* 28 */     Trip trip = new Trip(this.day, this.vehicle);
/* 29 */     updateTime(1800.0D);
/* 30 */     this.trips.add(trip);
/* 31 */     this.noTripAdded = false;
/*    */   }
/*    */ 
/*    */   public boolean hasEnoughTimeFor(double timeDiff) {
/* 35 */     if (this.endTime + timeDiff > 43200.0D) {
/* 36 */       return false;
/*    */     }
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   public double timeScoreDiffWouldBe(double timeDiff)
/*    */   {
/* 43 */     if (timeDiff == -1.0D) {
/* 44 */       return -Math.max(0.0D, (this.endTime - 43200.0D) * 5.0D);
/*    */     }
/* 46 */     return Math.max(0.0D, this.endTime + timeDiff - 43200.0D) * 5.0D - this.timeScore;
/*    */   }
/*    */ 
/*    */   public void updateTime(double timeDiff)
/*    */   {
/* 51 */     this.endTime += timeDiff;
/* 52 */     this.timeScore = (Math.max(0.0D, this.endTime - 432000.0D) * 5.0D);
/*    */   }
/*    */ }