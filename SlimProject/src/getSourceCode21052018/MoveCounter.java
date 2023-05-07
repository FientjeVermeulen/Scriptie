package getSourceCode21052018;

/*    */ import java.io.PrintStream;
import java.io.Serializable;
/*    */ 
/*    */ public class MoveCounter extends Counter implements Serializable
/*    */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*    */   int better;
/*    */   int accepted;
/*    */   int worseAccepted;
/*    */   int denied;
/*    */ 
/*    */   public MoveCounter(int no, String nm, Counters ctrs)
/*    */   {
/* 11 */     super(no, nm, ctrs);
/* 12 */     this.better = 0;
/* 13 */     this.accepted = 0;
/* 14 */     this.worseAccepted = 0;
/* 15 */     this.denied = 0;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/* 20 */     super.reset();
/* 21 */     this.better = 0;
/* 22 */     this.accepted = 0;
/* 23 */     this.worseAccepted = 0;
/* 24 */     this.denied = 0;
/*    */   }
/*    */ 
/*    */   public void better() {
/* 28 */     this.better += 1;
/* 29 */     this.worseAccepted -= 1;
/*    */   }
/*    */ 
/*    */   public void accepted() {
/* 33 */     this.accepted += 1;
/* 34 */     this.worseAccepted += 1;
/*    */   }
/*    */ 
/*    */   public void denied()
/*    */   {
/* 39 */     this.denied += 1;
/*    */   }
/*    */ 
/*    */   public void print()
/*    */   {
/* 44 */     System.out.println();
/* 45 */     System.out.println("Move - " + this.name + ": " + this.number);
/* 46 */     System.out.println("Times better: " + this.better);
/* 47 */     System.out.println("Times accepted: " + this.accepted);
/* 48 */     System.out.println("Times worse but accepted: " + this.worseAccepted);
/* 49 */     System.out.println("Times denied: " + this.denied);
/*    */   }
/*    */ }