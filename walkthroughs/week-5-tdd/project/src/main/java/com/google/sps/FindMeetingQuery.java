// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ArrayList;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<Event> relevantEvents = getRelevantEvents(events, request.getAttendees());
    Collections.sort(relevantEvents);
    int meetingStart = 0;
    Collection<TimeRange> possibleTimeRanges = new ArrayList<TimeRange>();
    for (Event event : relevantEvents) {
      int eventStart = event.getWhen().start();
      int eventEnd = event.getWhen().end();
      if (eventStart - meetingStart >= request.getDuration()) {
        possibleTimeRanges.add(TimeRange.fromStartDuration(meetingStart, eventStart - meetingStart));
      }
      meetingStart = meetingStart > eventEnd ? meetingStart : eventEnd;
    }
    if (24*60 - meetingStart >= request.getDuration()) {
      possibleTimeRanges.add(TimeRange.fromStartDuration(meetingStart, 24*60 - meetingStart));
    }
    return possibleTimeRanges;
  }

  public ArrayList<Event> getRelevantEvents(Collection<Event> events, Collection<String> attendees) {
    ArrayList<Event> relevantEvents = new ArrayList<Event>();
    for (Event event: events) {
      Collection<String> intersection = new HashSet<>(event.getAttendees());
      intersection.retainAll(attendees);
      if (intersection.size() != 0) {
        relevantEvents.add(event);
      }
    };
    return relevantEvents;
  }
}
