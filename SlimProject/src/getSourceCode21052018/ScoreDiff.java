package getSourceCode21052018;

import java.io.Serializable;

/*    */ public class ScoreDiff implements Serializable
/*    */ {

	       private static final long serialVersionUID = 1L;
/*    */   int day;
/*    */   ScheduleEntry se;
/*    */   ScheduleEntry indicator;
/*    */   double timeDiff;
/*    */   double declineDiff;
/*    */   double volScoreDiff;
		   double fisfactor;
/*    */ 
/*    */   public ScoreDiff(int d, ScheduleEntry s, ScheduleEntry indic, double timeD, double declineD, double volD)
/*    */   {
/* 13 */     this.se = s;
/* 14 */     this.day = d;
/* 15 */     this.indicator = indic;
/* 16 */     this.timeDiff = timeD;
/* 17 */     this.declineDiff = declineD;
/* 18 */     this.volScoreDiff = volD;
/*    */   }
/*    */ 
/*    */   public void update(ScheduleEntry indic, double timeD, double declineD, double volD, double fsfctr) {
/* 22 */     this.indicator = indic;
/* 23 */     this.timeDiff = timeD;
/* 24 */     this.declineDiff = declineD;
/* 25 */     this.volScoreDiff = volD;
			 this.fisfactor = fsfctr;
/*    */   }
/*    */ }
