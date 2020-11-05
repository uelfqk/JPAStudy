package domain;

public class TestClass {
    private String cpu;

    private String hdd;

    private boolean isGraphicCardEnabled;
    private boolean isBluetoothEnabled;

    public String getCpu() {
        return cpu;
    }

    public String getHdd() {
        return hdd;
    }

    public boolean isGraphicCardEnabled() {
        return isGraphicCardEnabled;
    }

    public boolean isBluetoothEnabled() {
        return isBluetoothEnabled;
    }

    private TestClass(TestClassBuilder builder) {
        this.cpu = builder.cpu;
        this.hdd = builder.hdd;
        this.isGraphicCardEnabled = builder.isGraphicCardEnabled;
        this.isBluetoothEnabled = builder.isBluetoothEnabled;
    }

    public static class TestClassBuilder {
        private String cpu;
        private String hdd;

        private boolean isGraphicCardEnabled;
        private boolean isBluetoothEnabled;

        public TestClassBuilder(String cpu, String hdd) {
            this.cpu = cpu;
            this.hdd = hdd;
        }

        public TestClassBuilder setGraphicCardEnabled(boolean isGraphicCardEnabled) {
            this.isGraphicCardEnabled = isGraphicCardEnabled;
            return this;
        }

        public TestClassBuilder setBluetoothEnabled(boolean isBluetoothEnabled) {
            this.isBluetoothEnabled = isBluetoothEnabled;
            return this;
        }

        public TestClass build() {
            return new TestClass(this);
        }
    }
}
