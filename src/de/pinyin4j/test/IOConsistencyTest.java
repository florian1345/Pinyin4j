package de.pinyin4j.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiPredicate;

import static org.junit.Assert.assertTrue;

final class IOConsistencyTest {

    @FunctionalInterface
    interface ISaver<T> {
        void save(T toSave, DataOutputStream stream) throws IOException;
    }

    @FunctionalInterface
    interface ILoader<T> {
        T load(DataInputStream stream) throws IOException;
    }

    private IOConsistencyTest() { }

    static <T> void run(ISaver<? super T> saver, ILoader<? extends T> loader,
            BiPredicate<? super T, ? super T> equator, T testedObject) throws IOException {
        final File tmpFile = new File("tmp.tmp");
        if (!tmpFile.exists()) assertTrue("Could not create file", tmpFile.createNewFile());
        final DataOutputStream outStream = new DataOutputStream(new FileOutputStream(tmpFile));
        saver.save(testedObject, outStream);
        outStream.close();
        final DataInputStream inStream = new DataInputStream(new FileInputStream(tmpFile));
        final T loadedObject = loader.load(inStream);
        inStream.close();
        assertTrue("Could not delete file. This may mess up future tests if not fixed.",
            tmpFile.delete());
        assertTrue("Saved and loaded objects are not equal.",
            equator.test(testedObject, loadedObject));
    }
}
