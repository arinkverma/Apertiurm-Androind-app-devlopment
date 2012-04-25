/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apertium.transfer.development;

import java.util.LinkedHashMap;

/**
 *
 * @author Jacob Nordfalk
 */
  public class Timing {
    public Timing(String name) { this.name=name; }
    public String name;
    public long timing = System.nanoTime();
    public LinkedHashMap<String, Long> times = new LinkedHashMap<String,Long>();
    public  void log(String task) {
      long now = System.nanoTime();
      long diff = now - timing;
      timing = now;
      //System.err.println("diff = " + diff);
      Long sum = times.get(task);
      sum = sum==null? diff : diff+sum;
      times.put(task, sum);
    }
    public void report() {
      long total = 0;
      for (Long l : times.values()) total += l;
      for (String task : times.keySet()) {
        long time = times.get(task);
        System.err.println(name+" used " + ((1000*time/total)/10.0)+ " % time for "+task);
      }
      System.err.println(name+" took total " + ((total/10000)/100.0)+ " sec");
    }
  }
