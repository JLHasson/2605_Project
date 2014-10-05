import Jama.Matrix;

/**
 * Created by Carey on 9/29/2014.
 */



public class MyMatrix {

    private Matrix a;
    private int rows, cols;
    private MyVector u, v;
    private double res = 0;

    public MyMatrix(double[][] m) {
        a = new Matrix(m);
        rows = m.length;
        cols = m[0].length;
    }

    public MyMatrix(Matrix m) {
        a = m;
        rows = m.getRowDimension();
        cols = m.getColumnDimension();
    }

    public MyMatrix(double[] m, int i) {
        a = new Matrix(m, i);
        rows = m.length;
        cols = 1;
    }

    public MyMatrix(int i, int j) {
        a = new Matrix(i, j);
        rows = i;
        cols = j;
    }

    public MyMatrix(MyVector ... vecs) {
        a = new Matrix(vecs[0].numRows(), vecs.length);
        for (int i = 0; i < vecs.length; i++) {
            for (int j = 0; j < vecs[0].numRows(); j++) {
                a.set(i, j, vecs[i].get(j));
            }
        }
        rows = vecs[0].numRows();
        cols = vecs.length;
    }

    private Matrix toMatrix() {
        return a;
    }

    public int numRows() { return rows; }

    public int numCols() { return cols; }

    public double get(int i, int j) { return a.get(i, j); }

    public void set(int i, int j, double data) { a.set(i, j, data); }

    public MyMatrix append(MyVector vec) {
        if (vec.numRows() != this.numRows()) { throw new RuntimeException("Invalid Vector"); }
        MyMatrix temp = new MyMatrix(this.numRows(), this.numCols() + 1);
        temp.print();
        for (int i = 0; i < temp.numRows(); i++) {
            for (int j = 0; j < temp.numCols(); j++) {
                if (j == temp.numCols() - 1) {
                    temp.set(i, j, vec.get(i));
                } else {
                    temp.set(i, j, this.get(i, j));
                }
            }
        }
        return temp;
    }

    public MyVector[] toVectors() {
        MyVector[] temp = new MyVector[this.numCols()];
        for (int i = 0; i < this.numCols(); i++) {
            double[] vec = new double[this.numRows()];
            for (int j = 0; j < this.numRows(); j++) {
                vec[j] = this.get(i, j);
            }
            temp[i] = new MyVector(vec);
        }
        return temp;
    }

    public MyMatrix plus(MyMatrix m) {
        return new MyMatrix(a.plus(m.toMatrix()));
    }

    public MyMatrix minus(MyMatrix m) {
        return new MyMatrix(a.minus(m.toMatrix()));
    }

    public MyMatrix scale(double coef) {
        return new MyMatrix(a.times(coef));
    }

    public MyMatrix transpose() {
        return new MyMatrix(a.transpose());
    }

    public MyMatrix times(MyMatrix m) {
        if (numCols() != m.numRows()) {
            throw new RuntimeException("Invalid Matrix");
        }
        Matrix res = new Matrix(a.getColumnDimension(), m.numRows());
        for (int i = 0; i < res.getRowDimension(); i++) {
            for (int j = 0; j < res.getColumnDimension(); j++) {
                double[] aY = new double[a.getRowDimension()];
                double[] bX = new double[m.numCols()];
                for (int n = 0; n < a.getColumnDimension(); n++) {
                    aY[n] = a.get(i, n);
                    bX[n] = m.get(n, j);
                }
                u = new MyVector(aY);
                v = new MyVector(bX);
                res.set(i, j, u.dot(v));
            }
        }
        return new MyMatrix(res);
    }

    public MyMatrix splice(int row, int col) {
        MyMatrix temp = new MyMatrix(numRows() - 1, numCols() - 1);
        for (int i = 0; i < numRows() - 1; i++) {
            for (int j = 0; j < numCols() - 1; j++) {
                if (i < row) {
                    if (j < col) {
                        temp.set(i, j, this.get(i, j));
                    } else {
                        temp.set(i, j, this.get(i, j + 1));
                    }
                } else {
                    if (j < col) {
                        temp.set(i, j, this.get(i + 1, j));
                    } else {
                        temp.set(i, j, this.get(i + 1, j + 1));
                    }
                }
            }
        }
        temp.print();
        System.out.println();
        return temp;
    }

    private double determinant(MyMatrix m) {
        /*if (m.numCols() == 2) {
            return (m.get(0,0) * m.get(1,1)) - (m.get(0,1) * m.get(1,0));
        } else {
            double res = 0;
            for (int n = 0; n < m.numRows(); n++) {
                MyMatrix temp = new MyMatrix(m.numRows() - 1, m.numCols() - 1);
                for (int i = 1; i < m.numRows(); i++) {
                    for (int j = 0; j < n; j++) {
                        temp.set(i - 1, j, m.get(i, j));
                    }
                    for (int j = n; j < m.numCols() - 1; j++) {
                        temp.set(i - 1, j, m.get(i, j + 1));
                    }
                }
                temp.print();
                System.out.println();
                if (n % 2 == 0) {
                    res = res + (m.get(0, n) * determinant(temp));
                } else {
                    res = res - (m.get(0, n) * determinant(temp));
                }
            }
            return res;
        }*/

        if (m.numCols() == 2) {
            return (m.get(0,0) * m.get(1,1)) - (m.get(0,1) * m.get(1,0));
        } else {
            double res = 0;
            for (int n = 0; n < m.numCols(); n++) {
                if (n % 2 == 0) {
                    res = res + (m.get(0, n) * determinant(m.splice(0, n)));
                } else {
                    res = res - (m.get(0, n) * determinant(m.splice(0, n)));
                }
            }
            return res;
        }
    }

    public double determinant() {
        if (this.numRows() != this.numCols() || this.numCols() < 2) {
            throw new RuntimeException("Invalid Matrix");
        }
        return determinant(new MyMatrix(a));
        }

    public MyMatrix cofactor() {
        MyMatrix temp1 = new MyMatrix(this.numRows(), this.numCols());
        System.out.println(temp1.numRows() + " x " + temp1.numCols());
            for (int i = 0; i < temp1.numRows(); i++) {
                for (int j = 0; j < temp1.numCols(); j++) {
                    if ((i + j) % 2 == 0) {
                        temp1.set(i, j, this.splice(i, j).determinant());
                    } else {
                        temp1.set(i, j, -1 * this.splice(i, j).determinant());
                    }
                }
            }
        System.out.println(temp1.numRows() + " x " + temp1.numCols());
        return temp1;
    }

    public MyMatrix inverse() {
        MyMatrix temp;
        temp = this.cofactor().transpose().scale(1/this.determinant());
        return temp;
    }

    public MyMatrix[] qRDecomp() {
        if (this.numRows() != this.numCols()) { throw new RuntimeException("Invalid Matrix"); }
        MyMatrix r = new MyMatrix(this.numRows(), this.numCols());
        MyVector[] aVecs = this.toVectors();
        MyVector[] u = new MyVector[this.numCols()];
        MyVector[] e = new MyVector[this.numCols()];
        u[0] = aVecs[0];
        e[0] = u[0].scale(1 / u[0].norm());
        for (int i = 1; i < this.numCols(); i++) {
            aVecs[i].print();
            int k = i;
            MyVector sum = u[0].zeroVect();
            while (k != -1) {
                sum = sum.plus(aVecs[i].projectOnTo(e[k]));
                k--;
            }
            u[i] = aVecs[i].minus(sum);
            e[i] = u[i].scale(1 / u[i].norm());
        }
        MyMatrix q = new MyMatrix(this.numRows(),0);
        for (int i = 0; i < this.numRows(); i++) {
            q = q.append(e[i]);
            for (int j = 0; j < this.numCols(); j++) {
                if (j < i) {
                    r.set(i, j, 0);
                } else {
                    r.set(i, j, aVecs[j].dot(e[i]));
                }
            }
        }
        MyMatrix[] res = new MyMatrix[2];
        res[0] = q;
        res[1] = r;
        return res;
    }

    public void print() {
        for (int i = 0; i < numRows(); i++) {
            for (int j = 0; j < numCols(); j++) {
                if (get(i, j) >= 0) { System.out.print(" "); }
                System.out.print(get(i,j) + " ");
            }
            System.out.println();
        }
    }

}
