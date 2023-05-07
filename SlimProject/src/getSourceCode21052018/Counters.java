package getSourceCode21052018;

import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class Counters implements Serializable
/*    */ {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*    */   ArrayList<Counter> counters;
/*    */ 
/*    */   public Counters()
/*    */   {
/* 11 */     this.counters = new ArrayList<Counter>();
/*    */   }
/*    */ 
/*    */   public void addCounter(Counter counter) {
/* 15 */     this.counters.add(counter);
/*    */   }
/*    */ 
/*    */   public void resetCounters() {
/* 19 */     for (Counter counter : this.counters)
/* 20 */       counter.reset();
/*    */   }
/*    */ 
/*    */   public void printCounters()
/*    */   {
/* 25 */     for (Counter counter : this.counters) {
/* 26 */       counter.print();
/*    */     }
/* 28 */     System.out.println();
/*    */   }
/*    */ }