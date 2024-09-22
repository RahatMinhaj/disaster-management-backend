package com.disaster.managementsystem.constant;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IncludedMimeType {
    public final static List<String> mimeTypes = Stream.of(
          "image/png",
                  "image/jpeg",
                  "application/pdf",
                  "application/msword",
                  "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
//                  "text/plain",
//                  "application/vnd.ms-excel",
//                  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
//                  "application/zip",
//                  "application/gzip",
//                  "video/x-m4v"
          )
      .collect(Collectors.toList());;
}