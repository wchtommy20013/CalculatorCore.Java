public class Status {
        public class Flag {
            public static final int None = 0;
            public static final int Dotted = 1;
            public static final int Operator_Pressed = 1 << 1;

            public static final int Operator_Just_Pressed = 1 << 2;

            public static final int Result_Display = 1 << 3;
        }

        private int flag = Flag.None;

        public Status() {

        }

        public void On(int flag) {
            this.flag |= flag;
        }

        public void Off(int flag) {
            this.flag &= ~flag;
        }

        public boolean Is(int flag) {
            return (this.flag & flag) != 0;
        }

        public boolean IsNot(int flag) {
            return (this.flag & flag) == 0;
        }

        public void ResetExcept(int flag) {
            this.flag &= flag;
        }

        public void Reset() {
            flag = 0;
        }
    }
