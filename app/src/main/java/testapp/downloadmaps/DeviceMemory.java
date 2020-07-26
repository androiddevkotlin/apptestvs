package testapp.downloadmaps;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class DeviceMemory {

    public static double getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    public static double getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    private static double formatSize(long size) {
        if (size >= 1024) {
            size /= 1024;
            if (size >= 1024) {
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, '.');
            commaOffset -= 3;
        }

        String text = resultBuffer.toString();

        return Double.parseDouble(text);
    }

}
