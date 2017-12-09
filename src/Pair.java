public class Pair<L,R> {

        private L left;
        private R right;

        public Pair() {
        }

        public void setLeft(L left) {
                this.left = left;
        }

        public void setRight(R right) {
                this.right = right;
        }

        public Pair(Pair<L, R>other){
                this.left = other.getLeft();
                this.right = other.getRight();
        }

        public Pair(L left, R right) {
                this.left = left;
                this.right = right;
        }

        public L getLeft() { return left; }
        public R getRight() { return right; }

        public Pair<L, R> clone(){
                Pair ret = new Pair(this.left, this.right);
                return ret;
        }

        public void print(){
                System.out.println(left.toString() + " " + right.toString());
        }

        @Override
        public int hashCode() { return left.hashCode() ^ right.hashCode(); }

        @Override
        public boolean equals(Object o) {
                if (!(o instanceof Pair)) return false;
                Pair pairo = (Pair) o;
                return this.left.equals(pairo.getLeft()) &&
                        this.right.equals(pairo.getRight());
        }

}