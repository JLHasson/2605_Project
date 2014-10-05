import Jama.Matrix;

/**
 * Created by Carey on 9/30/2014.
 */
public class MyVector {

    private Matrix v;
    double[] holder;

    public MyVector(double[] vec) {
        v = new Matrix(vec, vec.length);
    }

    public MyVector(int ... nums) {
        holder = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            holder[i] = nums[i];
        }
        v = new Matrix(holder, holder.length);
    }

    public MyVector(Matrix m) {
        if (m.getColumnDimension() != 1) { throw new RuntimeException("Invalid Vector Dimensions"); }
        else { v = m; }
    }

    public Matrix toMatrix() { return v; }

    public double get(int i) { return v.get(i, 0); }

    public int numRows() { return v.getRowDimension(); }

    public int numCols() { return 1; }

    public MyVector zeroVect() {
        double[] temp = new double[this.numRows()];
        for (int i = 0; i < this.numRows(); i++) {
            temp[i] = 0;
        }
        return new MyVector(temp);
    }

    public MyVector plus(MyVector u) {
        return new MyVector(v.plus(u.toMatrix()));
    }

    public MyVector minus(MyVector u) {
        return new MyVector(v.plus(u.toMatrix()));
    }

    public MyVector scale(double coef) {
        return new MyVector(v.times(coef));
    }

    public double norm() {
        double res = 0.0;
        for (int i = 0; i < numRows(); i++) {
            res += (get(i) * get(i));
        }
        res = Math.sqrt(res);
        return res;
    }

    public double dot(MyVector u) {
        double res = 0.0;
        for (int i = 0; i < this.numRows(); i++) {
            res += (this.get(i) * u.get(i));
        }
        return res;
    }

    public MyVector projectOnTo(MyVector u) {
        u = u.scale(this.dot(u) / (this.norm() * this.norm()));
        return u;
    }

    public MyMatrix transpose() { return new MyMatrix(v.transpose()); }


    public void print() {
        for (int i = 0; i < this.numRows(); i++) {
            System.out.print(" " + this.get(i));
        }
        System.out.println();
    }

}
