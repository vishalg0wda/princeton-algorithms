import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private boolean[][] grid;
    private WeightedQuickUnionUF wqu;

    public Percolation(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        grid = new boolean[n][n];
        wqu = new WeightedQuickUnionUF((n * n) + 2); // for applying "clever trick"
    }

    private int effectiveIndex(int row, int col) {
        return (row * size) + col;
    }

    public void open(int row, int col) {
        if (row > size || col > size) {
            throw new IndexOutOfBoundsException();
        }

        // for internal indexing
        row = row - 1; 
        col = col - 1;

        try {
            if (grid[row - 1][col]) {
                wqu.union(
                    effectiveIndex(row - 1, col),
                    effectiveIndex(row, col)
                );
            }
        } catch (IndexOutOfBoundsException e) {
            // System.out.println('!');
        } finally {
            try {
                if (grid[row + 1][col]) {
                    wqu.union(
                        effectiveIndex(row + 1, col),
                        effectiveIndex(row, col)
                    );
                }
            } catch (IndexOutOfBoundsException e) {
                // System.out.println('!');
            } finally {
                try {
                    if (grid[row][col - 1]) {
                        wqu.union(
                            effectiveIndex(row, col - 1),
                            effectiveIndex(row, col)
                        );
                    }
                } catch (IndexOutOfBoundsException e) {
                    // System.out.println('!');
                } finally {
                    try {
                        if (grid[row][col + 1]) {
                            wqu.union(
                                effectiveIndex(row, col + 1),
                                effectiveIndex(row, col)
                            );
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // System.out.println('!');
                    }
                }
            }
        }

        grid[row][col] = true;
        if (row == size-1) {
            // connect to lower virtual root
            int lvr = (size * size) + 1;
            wqu.union(
                effectiveIndex(row, col),
                lvr
            );
        } else if (row == 0) {
            // connect to upper virtual root
            int uvr = (size * size) + 0;
            wqu.union(
                effectiveIndex(row, col),
                uvr
            );
        }
    }

    public boolean isOpen(int row, int col) {
        if (row > size || col > size) {
            throw new IndexOutOfBoundsException();
        }
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if (row > size || col > size) {
            throw new IndexOutOfBoundsException();
        }

        // for internal indexing
        row = row - 1; 
        col = col - 1;

        if (row == 0) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (wqu.connected(
                effectiveIndex(row, col),
                effectiveIndex(0, i)
            )) {
                return true;
            }
        }
        return false;
    }

    public boolean percolates() {
        for (int i = 0; i < size; ++i) {
        }
        int gridSize = size * size;
        return wqu.connected(gridSize, gridSize + 1);
    }

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        Percolation p = new Percolation(size);
        System.out.println(Boolean.toString(p.isFull(1, 6)));
    }
}