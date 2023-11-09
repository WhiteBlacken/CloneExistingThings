package com.jvm.rtda;

/**
 * @Author qxy
 * @Date 2023/11/9 1:23
 * @Version 1.0
 */
public class LocalVars {

    private Slot[] slots;

    LocalVars(int maxLocals) {
        if (maxLocals > 0) {
            slots = new Slot[maxLocals];
            for (int i = 0; i < maxLocals; i++) {
                slots[i] = new Slot();
            }
        }
    }

    public void setInt(int idx, int val) {
        this.slots[idx].num = val;
    }

    public int getInt(int idx) {
        return slots[idx].num;
    }

    public void setFloat(int idx, float val) {
        this.slots[idx].num = Float.floatToRawIntBits(val);
    }

    public Float getFloat(int idx) {
        int num = this.slots[idx].num;
        return Float.intBitsToFloat(num);
    }

    public void setLong(int idx, long val) {
        this.slots[idx].num = (int) val;
        this.slots[idx + 1].num = (int) (val >> 32);
    }

    public Long getLong(int idx) {
        int low = this.slots[idx].num;
        int high = this.slots[idx + 1].num;
        return ((long) high << 32) | (long) low;
    }


    public void setDouble(int idx, double val){
        setLong(idx, Double.doubleToLongBits(val));
    }

    public Double getDouble(int idx){
        return Double.longBitsToDouble(getLong(idx));
    }

    public void setRef(int idx, Object ref){
         slots[idx].ref = ref;
    }

    public Object getRef(int idx){
        return slots[idx].ref;
    }

    public Slot[] getSlots(){
        return slots;
    }
}
