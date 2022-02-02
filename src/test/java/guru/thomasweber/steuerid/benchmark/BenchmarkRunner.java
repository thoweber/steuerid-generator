package guru.thomasweber.steuerid.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import guru.thomasweber.steuerid.SteuerIdGenerator;

public class BenchmarkRunner {
	
	@State(Scope.Thread)
	public static class StateObj {
		public SteuerIdGenerator generator;
		
		@Setup
		public void setup() {
			this.generator = new SteuerIdGenerator();
		}
		
	}
	
	@Benchmark
	@Fork(value = 1, warmups = 2)
	@BenchmarkMode(Mode.Throughput)
	public String generateThroughput(StateObj state) {
	    return state.generator.generate();
	}
	
	public static void main(String[] args) throws Exception {
		Options opt = new OptionsBuilder()
                .include(BenchmarkRunner.class.getSimpleName())
                .forks(2)
                .warmupIterations(2)
                .measurementIterations(2)
                .build();
        new Runner(opt).run();
    }
	
}