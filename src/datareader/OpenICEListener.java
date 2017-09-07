package datareader;

import org.mdpnp.rtiapi.data.QosProfiles;
import org.mdpnp.rtiapi.qos.IceQos;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.PublisherQos;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderListener;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.LivelinessChangedStatus;
import com.rti.dds.subscription.RequestedDeadlineMissedStatus;
import com.rti.dds.subscription.RequestedIncompatibleQosStatus;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleLostStatus;
import com.rti.dds.subscription.SampleRejectedStatus;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.SubscriberQos;
import com.rti.dds.subscription.SubscriptionMatchedStatus;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

import deviceManager.DeviceManager;
import ice.NumericDataReader;
import ice.SampleArrayDataReader;

public class OpenICEListener implements Runnable {
	
	private DeviceManager deviceManager = null;

	public void receiveOnMiddlewareThread(final DomainParticipant participant, final Topic sampleArrayTopic,
			final Topic numericTopic) {

		// A listener to receive callback events from the SampleArrayDataReader
		final DataReaderListener saListener = new DataReaderListener() {

			@Override
			public void on_data_available(DataReader reader) {
				// Will contain the data samples we read from the reader
				ice.SampleArraySeq sa_data_seq = new ice.SampleArraySeq();

				// Will contain the SampleInfo information about those data
				SampleInfoSeq info_seq = new SampleInfoSeq();

				SampleArrayDataReader saReader = (SampleArrayDataReader) reader;

				// Read samples from the reader
				try {
					saReader.read(sa_data_seq, info_seq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
							SampleStateKind.NOT_READ_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
							InstanceStateKind.ALIVE_INSTANCE_STATE);

					// Iterator over the samples
					for (int i = 0; i < info_seq.size(); i++) {
						SampleInfo si = (SampleInfo) info_seq.get(i);
						ice.SampleArray data = (ice.SampleArray) sa_data_seq.get(i);
						// If the updated sample status contains fresh data that
						// we can evaluate
						if (si.valid_data) {
//							System.out.println(data);
//							deviceManager.sendDaTa(data.toString());
							
						}

					}
				} catch (RETCODE_NO_DATA noData) {
					// No Data was available to the read call
				} finally {
					// the objects provided by "read" are owned by the reader
					// and we must return them
					// so the reader can control their lifecycle
					saReader.return_loan(sa_data_seq, info_seq);
				}
			}

			@Override
			public void on_liveliness_changed(DataReader arg0, LivelinessChangedStatus arg1) {
				System.out.println("liveliness_changed " + arg1);
			}

			@Override
			public void on_requested_deadline_missed(DataReader arg0, RequestedDeadlineMissedStatus arg1) {
				System.out.println("requested_deadline_missed " + arg1);
			}

			@Override
			public void on_requested_incompatible_qos(DataReader arg0, RequestedIncompatibleQosStatus arg1) {
				System.out.println("requested_incompatible_qos " + arg1);
			}

			@Override
			public void on_sample_lost(DataReader arg0, SampleLostStatus arg1) {
				System.out.println("sample_lost " + arg1);
			}

			@Override
			public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1) {
				System.out.println("sample_rejected " + arg1);
			}

			@Override
			public void on_subscription_matched(DataReader arg0, SubscriptionMatchedStatus arg1) {
				System.out.println("subscription_matched " + arg1);
			}

		};

		// A listener to receive callback events from the NumericDataReader
		final DataReaderListener nListener = new DataReaderListener() {
			@Override
			public void on_data_available(DataReader reader) {
				ice.NumericSeq n_data_seq = new ice.NumericSeq();

				// Will contain the SampleInfo information about those data
				SampleInfoSeq info_seq = new SampleInfoSeq();

				NumericDataReader nReader = (NumericDataReader) reader;

				try {
					// Read samples from the reader
					nReader.read(n_data_seq, info_seq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
							SampleStateKind.NOT_READ_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
							InstanceStateKind.ALIVE_INSTANCE_STATE);

					// Iterator over the samples
					for (int i = 0; i < info_seq.size(); i++) {
						SampleInfo si = (SampleInfo) info_seq.get(i);
						ice.Numeric data = (ice.Numeric) n_data_seq.get(i);
						// If the updated sample status contains fresh data that
						// we can evaluate
						if (si.valid_data) {
							if (data.metric_id.equals(rosetta.MDC_PULS_OXIM_SAT_O2.VALUE)) {
								// This is an O2 saturation from pulse oximetry
								// System.out.println("SpO2="+data.value);
							} else if (data.metric_id.equals(rosetta.MDC_PULS_OXIM_PULS_RATE.VALUE)) {
								// This is a pulse rate from pulse oximetry
								// System.out.println("Pulse Rate="+data.value);
							}
//							System.out.println(data);
//							data.presentation_time.toString();
							deviceManager.sendDaTa(data);
						}

					}
				} catch (RETCODE_NO_DATA noData) {
					// No Data was available to the read call
					System.out.println("sem dados!");
				} finally {
					// the objects provided by "read" are owned by the reader
					// and we must return them
					// so the reader can control their lifecycle
					nReader.return_loan(n_data_seq, info_seq);
				}

			}

			@Override
			public void on_liveliness_changed(DataReader arg0, LivelinessChangedStatus arg1) {
				System.out.println("liveliness_changed " + arg1);
			}

			@Override
			public void on_requested_deadline_missed(DataReader arg0, RequestedDeadlineMissedStatus arg1) {
				System.out.println("requested_deadline_missed " + arg1);
			}

			@Override
			public void on_requested_incompatible_qos(DataReader arg0, RequestedIncompatibleQosStatus arg1) {
				System.out.println("requested_incompatible_qos " + arg1);
			}

			@Override
			public void on_sample_lost(DataReader arg0, SampleLostStatus arg1) {
				System.out.println("sample_lost " + arg1);
			}

			@Override
			public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1) {
				System.out.println("sample_rejected " + arg1);
			}

			@Override
			public void on_subscription_matched(DataReader arg0, SubscriptionMatchedStatus arg1) {
				System.out.println("subscription_matched " + arg1);
			}
		};

		// Create a reader endpoint for samplearray data
		@SuppressWarnings("unused")
		ice.SampleArrayDataReader saReader = (ice.SampleArrayDataReader) participant.create_datareader_with_profile(
				sampleArrayTopic, QosProfiles.ice_library, QosProfiles.waveform_data, saListener,
				StatusKind.STATUS_MASK_ALL);

		@SuppressWarnings("unused")
		ice.NumericDataReader nReader = (ice.NumericDataReader) participant.create_datareader_with_profile(numericTopic,
				QosProfiles.ice_library, QosProfiles.numeric_data, nListener, StatusKind.STATUS_MASK_ALL);

	}

	@Override
	public void run() {
		int domainId = 0;

		// Here we use 'default' Quality of Service settings supplied by
		// x73-idl-rti-dds
		IceQos.loadAndSetIceQos();

		// A domain participant is the main access point into the DDS domain.
		// Endpoints are created within the domain participant
		DomainParticipant participant = DomainParticipantFactory.get_instance().create_participant(domainId,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);

		// OpenICE divides the global DDS data-space into individual patient
		// contexts using the DDS partitioning mechanism.
		// Partitions are a list of strings (and wildcards) that pubs and subs
		// are required to match (at least one once) in their respective lists
		// to pair.
		// Partitions are assigned at the publisher/subscriber level in the
		// quality of service (QoS) settings.

		// Declare and instantiate a new SubscriberQos policy
		SubscriberQos subscriberQos = new SubscriberQos();

		// Populate the SubscriberQos policy
		participant.get_default_subscriber_qos(subscriberQos);

		subscriberQos.partition.name.clear();

		// Add an entry to the partition list. 'name' is actually a DDS
		// StringSeq that you can simply .add() to.
		// To receive OpenICE data for a specific patient, add the patient's MRN
		// to the QoS partition policy
		// e.g. "MRN=12345". MRNs are alphanumeric prefixed with "MRN="
		// subscriberQos.partition.name.add("MRN=10101"); // This is fake
		// patient Randall Jones in the MDPnP lab.

		// Partitioning supports some wildcards like "*" to access every
		// partition (including the default or null partition)
		subscriberQos.partition.name.add("*");

		// A note about partition names:
		// The Supervisor uses an MRN for the Partition name and a First / Last
		// name as a display name. For example, MRN=10101
		// will show up as Randall Jones in the Supervisor. To match display
		// names and MRNs, the Supervisor will either
		// use defaults or look them up in an HL7 FHIR database. If you provide
		// an HL7 FHIR server address, the Supervisor
		// will treat that server as a "Master Patient Index". The Supervisor
		// will attempt to download the Patient Resource
		// (https://www.hl7.org/fhir/patient.html) from that address and use
		// resource.identifier.value as the Partition name and
		// resource.name.family / resource.name.given as the display name. If no
		// HL7 FHIR address is provided, the Supervisor will
		// provide a small SQL server of default names and MRNs.

		// Set the subscriber qos with our newly created SubscriberQos
		participant.set_default_subscriber_qos(subscriberQos);

		// There are a couple ways to do this (as far as I can tell). I don't
		// know which one is correct or standard or proper or whatever. For
		// example:
		// Subscriber subscriber = participant.get_implicit_subscriber();
		// subscriber.get_qos(subscriberQos);
		// subscriberQos.partition.name.add("MRN=10101");
		// subscriber.set_qos(subscriberQos);

		// Same concept but this time for the publisher
		PublisherQos publisherQos = new PublisherQos();

		participant.get_default_publisher_qos(publisherQos);

		publisherQos.partition.name.clear();

		// Change this line to the patient MRN for which you want to emit data.
		// publisherQos.partition.name.add("MRN=10101");

		participant.set_default_publisher_qos(publisherQos);

		// Inform the participant about the sample array data type we would like
		// to use in our endpoints
		ice.SampleArrayTypeSupport.register_type(participant, ice.SampleArrayTypeSupport.get_type_name());

		// Inform the participant about the numeric data type we would like to
		// use in our endpoints
		ice.NumericTypeSupport.register_type(participant, ice.NumericTypeSupport.get_type_name());

		// A topic the mechanism by which reader and writer endpoints are
		// matched.
		Topic sampleArrayTopic = participant.create_topic(ice.SampleArrayTopic.VALUE,
				ice.SampleArrayTypeSupport.get_type_name(), DomainParticipant.TOPIC_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);

		// A second topic if for Numeric data
		Topic numericTopic = participant.create_topic(ice.NumericTopic.VALUE, ice.NumericTypeSupport.get_type_name(),
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);

		receiveOnMiddlewareThread(participant, sampleArrayTopic, numericTopic);

	}

	public void setDeviceManager(DeviceManager newDeviceManager) {
		this.deviceManager = newDeviceManager;
		
	}
}
