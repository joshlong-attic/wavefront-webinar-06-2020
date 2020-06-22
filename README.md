# The code for the  Wavefront Webinar on 06-24-2020 

This sample code demonstrates the interactions between numerous services and demonstrates how those services can be observed with the wünderkind observability platform from Tanzu, Wavefront. 

The hypothetical flow is: 

```
`api-gateway` ->(HTTP)-> (
    `payments` -> (Kafka)-> `fulfillment`   
    `payments` -> (HTTP)-> `customer-satisfaction`
    )
  ```
