package org.apertium.android.SMS;

public class SMSobject implements Comparable<SMSobject>{
    private String body;
    private String sender;
    private Long date;
    
    public SMSobject(String b,String s,Long d)
    {
        body = b;
        sender = s;
        date = d;
    }
    public String getBody()
    {
        return body;
    }
    public String getSender()
    {
        return sender;
    }
    
    public Long getDate()
    {
        return date;
    }
  
    @Override
    public int compareTo(SMSobject o) {
        if(this.body != null)
            return this.date.compareTo(o.getDate()); //this.body.toLowerCase().compareTo(o.getBody().toLowerCase()); 
        else 
            throw new IllegalArgumentException();
    }
}
