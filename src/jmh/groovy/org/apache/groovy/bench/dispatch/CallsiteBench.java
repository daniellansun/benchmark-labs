/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.groovy.bench.dispatch;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CallsiteBench {
    private static final int LOOP_TIMES = 1_000_000;

    @Benchmark
    public void dispatch_monomorphic(MonomorphicState state, Blackhole bh) {
        for (int i = 0; i < LOOP_TIMES; i++) {
            GroovyCallsite.dispatch(state.receivers, bh);
        }
    }

    @Benchmark
    public void dispatch_polymorphic(PolymorphicState state, Blackhole bh) {
        for (int i = 0; i < LOOP_TIMES; i++) {
            GroovyCallsite.dispatch(state.receivers, bh);
        }
    }

    @Benchmark
    public void dispatch_megamorphic(MegamorphicState state, Blackhole bh) {
        for (int i = 0; i < LOOP_TIMES; i++) {
            GroovyCallsite.dispatch(state.receivers, bh);
        }
    }

    private static final int RECEIVER_COUNT = 64;

    private static final Object[] RECEIVERS = new Object[] {
            new Receiver1(), new Receiver2(), new Receiver3(), new Receiver4(),
            new Receiver5(), new Receiver6(), new Receiver7(), new Receiver8()
    };

    @State(Scope.Thread)
    public static class MonomorphicState {
        Object[] receivers;
        @Setup(Level.Trial)
        public void setUp() {
            receivers = new Object[RECEIVER_COUNT];
            Arrays.fill(receivers, RECEIVERS[0]);
        }
    }

    @State(Scope.Thread)
    public static class PolymorphicState {
        final Random random = new Random();
        Object[] receivers;
        @Setup(Level.Iteration)
        public void setUp() {
            receivers = new Object[RECEIVER_COUNT];
            for (int i = 0; i < RECEIVER_COUNT; i++) {
                receivers[i] = RECEIVERS[random.nextInt(3)];
            }
        }
    }

    @State(Scope.Thread)
    public static class MegamorphicState {
        final Random random = new Random();
        Object[] receivers;
        @Setup(Level.Iteration)
        public void setUp() {
            receivers = new Object[RECEIVER_COUNT];
            for (int i = 0; i < RECEIVER_COUNT; i++) {
                receivers[i] = RECEIVERS[random.nextInt(8)];
            }
        }
    }

    private static class Receiver1 {
        @Override public String toString() { return "receiver1"; }
    }

    private static class Receiver2 {
        @Override public String toString() { return "receiver2"; }
    }

    private static class Receiver3 {
        @Override public String toString() { return "receiver3"; }
    }

    private static class Receiver4 {
        @Override public String toString() { return "receiver4"; }
    }

    private static class Receiver5 {
        @Override public String toString() { return "receiver5"; }
    }

    private static class Receiver6 {
        @Override public String toString() { return "receiver6"; }
    }

    private static class Receiver7 {
        @Override public String toString() { return "receiver7"; }
    }

    private static class Receiver8 {
        @Override public String toString() { return "receiver8"; }
    }

}
